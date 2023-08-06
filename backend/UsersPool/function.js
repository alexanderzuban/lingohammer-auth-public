const AWS = require('aws-sdk');
const ssm = new AWS.SSM();
const cognito = new AWS.CognitoIdentityServiceProvider();

exports.handler = async (event, context) => {
    console.log('Event:');
    console.log(JSON.stringify(event));
    
    const { RequestType, ResourceProperties } = event;
    const userPoolId = ResourceProperties.UserPoolId;
    const clientId = ResourceProperties.UserPoolClientId;
    const parameterName = ResourceProperties.ParameterName;

    try {
        if (RequestType === 'Create' || RequestType === 'Update') {
            // Get the User Pool Client details
            const userPoolClient = await cognito.describeUserPoolClient({
                UserPoolId: userPoolId,
                ClientId: clientId
            }).promise();

            const clientSecret = userPoolClient.UserPoolClient.ClientSecret; 

            // Write the Client Secret to SSM Parameter Store

            await ssm.putParameter({
                Name: parameterName +"/userPoolId",
                Value: userPoolId,
                Type: 'SecureString',
                Overwrite: true
            }).promise();

            await ssm.putParameter({
                Name: parameterName +"/userPoolClientId",
                Value: clientId,
                Type: 'SecureString',
                Overwrite: true
            }).promise();

            await ssm.putParameter({
                Name: parameterName +"/userPoolClientSecret",
                Value: clientSecret,
                Type: 'SecureString',
                Overwrite: true
            }).promise();

           

            // Send a response back to CloudFormation indicating success
            return sendResponse(event, context, 'SUCCESS',clientSecret);
        } else if (RequestType === 'Delete') {
            try{
                // If it's a Delete event, you may choose to delete the SSM parameter if needed
                await ssm.deleteParameter({
                   Name: parameterName +"/userPoolId"
                }).promise();

                await ssm.deleteParameter({
                   Name: parameterName +"/userPoolClientId"
                }).promise();

                await ssm.deleteParameter({
                    Name: parameterName +"/userPoolClientSecret"
                 }).promise();
            }catch(ignore){
                console.error('Failed to delete parameter:', ignore.message);
            }

            // Send a response back to CloudFormation indicating success
            return sendResponse(event, context, 'SUCCESS',"");
        } else {
            console.error('Error: Invalid RequestType');
            return sendResponse(event, context, 'SUCCESS',"");
        }
    } catch (error) {
        console.error('Error:', error.message);        
        //do not fail the CFN stack
        return sendResponse(event, context, 'SUCCESS',"");
    }
};

function sendResponse(event, context, responseStatus,clientSecret) {
    var response = require('cfn-response');
    response.send(event, context, responseStatus, {
        ClientSecret: clientSecret
    }); 
}