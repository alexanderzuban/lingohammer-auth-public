import boto3
import cfnresponse

ssm = boto3.client('ssm')
cognito = boto3.client('cognito-idp')

def handler(event, context):
    print('Event:')
    print(event)
    
    request_type = event['RequestType']
    resource_properties = event['ResourceProperties']
    user_pool_id = resource_properties['UserPoolId']
    client_id = resource_properties['UserPoolClientId']
    parameter_name = resource_properties['ParameterName']
    try:
        if request_type == 'Create' or request_type == 'Update':
            # Get the User Pool Client details
            user_pool_client = cognito.describe_user_pool_client(
                UserPoolId=user_pool_id,
                ClientId=client_id
            )

            client_secret = user_pool_client['UserPoolClient']['ClientSecret']

            # Write the Client Secret to SSM Parameter Store
            put_parameter(parameter_name + "/userPoolId",user_pool_id)
            put_parameter(parameter_name + "/userPoolClientId",client_id)    
            put_parameter(parameter_name + "/userPoolClientSecret",client_secret)

            # Send a response back to CloudFormation indicating success
            return send_response(event, context, 'SUCCESS', client_secret)
        elif request_type == 'Delete':
        
                # If it's a Delete event, you may choose to delete the SSM parameter if needed
                delete_parameter(parameter_name + "/userPoolId")
                delete_parameter(parameter_name + "/userPoolClientId")
                delete_parameter(parameter_name + "/userPoolClientSecret")
                
                return send_response(event, context, 'SUCCESS')
    except Exception as e:
        print(e)
        return send_response(event, context, 'FAILED')

def delete_parameter(name):
    try:
        ssm.delete_parameter(Name=name)
    except Exception as e:
        print(e)      

def put_parameter(name, value):
    try:
        ssm.put_parameter(
            Name=name,
            Value=value,
            Type="SecureString",
            Overwrite=True
        )
    except Exception as e:
        print(e)
     

def send_response(event, context, response_status, client_secret=""):             
    responseData = {}
    responseData['Data'] = client_secret
    cfnresponse.send(event, context, cfnresponse.SUCCESS, responseData, "CustomResourcePhysicalID")