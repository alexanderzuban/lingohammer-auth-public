package com.lingohammer.aws.auth;

import com.lingohammer.aws.auth.data.IsFlawed;
import com.lingohammer.aws.auth.process.info.UserInfoRequest;
import com.lingohammer.aws.auth.process.info.UserInfoResponse;
import com.lingohammer.aws.auth.process.login.LoginRequest;
import com.lingohammer.aws.auth.process.login.LoginResponse;
import com.lingohammer.aws.auth.process.register.RegistrationConfirmRequest;
import com.lingohammer.aws.auth.process.register.RegistrationConfirmResponse;
import com.lingohammer.aws.auth.process.register.RegistrationRequest;
import com.lingohammer.aws.auth.process.register.RegistrationResponse;
import com.lingohammer.aws.auth.process.restore.RestorePasswordConfirmRequest;
import com.lingohammer.aws.auth.process.restore.RestorePasswordConfirmResponse;
import com.lingohammer.aws.auth.process.restore.RestorePasswordRequest;
import com.lingohammer.aws.auth.process.restore.RestorePasswordResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "LingoHammer Auth Service",
                version = "1.0.0",
                description = "LingoHammer Auth Service"
        )
)
@Produces("application/json")
@Consumes("application/json")
public interface AuthService {

    public static final String DOCUMENTATION_ONLY = "Documentation only";

    @Path("/auth/user/login")
    @POST
    @Operation(summary = "Login User",
            method = "POST",
            tags = {"auth"},
            description = "",
            parameters = {
                    @Parameter(name = "body",
                            description = "User to login",
                            required = true,
                            schema = @Schema(implementation = LoginRequest.class))
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    schema = @Schema(implementation = LoginResponse.class)
                            )),
                    @ApiResponse(responseCode = "400",
                            description = "Error",
                            content = @Content(
                                    schema = @Schema(implementation = IsFlawed.class)
                            )),
                    @ApiResponse(responseCode = "401",
                            description = "Invalid Credentials",
                            content = @Content(
                                    schema = @Schema(implementation = IsFlawed.class)
                            ))
            })
    LoginResponse login(LoginRequest request);

    @Path("/auth/user/register")
    @POST
    @Operation(summary = "Register User",
            method = "POST",
            tags = {"auth"},
            description = "",
            parameters = {
                    @Parameter(name = "body",
                            description = "User to register",
                            required = true,
                            schema = @Schema(implementation = RegistrationRequest.class))
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    schema = @Schema(implementation = RegistrationResponse.class)
                            )),
                    @ApiResponse(responseCode = "400",
                            description = "Error",
                            content = @Content(
                                    schema = @Schema(implementation = IsFlawed.class)
                            ))
            })
    RegistrationResponse register(RegistrationRequest request);

    @Path("/auth/user/confirm")
    @POST
    @Operation(summary = "Confirm User Registration",
            method = "POST",
            tags = {"auth"},
            description = "",
            parameters = {
                    @Parameter(name = "body",
                            description = "User to confirm",
                            required = true,
                            schema = @Schema(implementation = RegistrationConfirmRequest.class))
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    schema = @Schema(implementation = RegistrationConfirmResponse.class)
                            )),
                    @ApiResponse(responseCode = "400",
                            description = "Error",
                            content = @Content(
                                    schema = @Schema(implementation = IsFlawed.class)
                            ))
            })
    RegistrationConfirmResponse confirmRegistration(RegistrationConfirmRequest request);

    @Path("/auth/user")
    @POST
    @Operation(summary = "Get User/Refresh Token",
            method = "POST",
            tags = {"auth"},
            description = "",
            parameters = {
                    @Parameter(name = "token",
                            description = "Refresh token request",
                            required = true,
                            schema = @Schema(implementation = UserInfoRequest.class))
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    schema = @Schema(implementation = UserInfoResponse.class)
                            )),
                    @ApiResponse(responseCode = "400",
                            description = "Error",
                            content = @Content(
                                    schema = @Schema(implementation = IsFlawed.class)
                            ))
            })
    UserInfoResponse getUser(UserInfoRequest request);

    @Path("/auth/user/password/reset")
    @POST
    @Operation(summary = "Reset User Password",
            method = "POST",
            tags = {"auth"},
            description = "",
            parameters = {
                    @Parameter(name = "token",
                            description = "Password reset request",
                            required = true,
                            schema = @Schema(implementation = RestorePasswordRequest.class))
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    schema = @Schema(implementation = RestorePasswordResponse.class)
                            )),
                    @ApiResponse(responseCode = "400",
                            description = "Error",
                            content = @Content(
                                    schema = @Schema(implementation = IsFlawed.class)
                            ))
            })
    RestorePasswordResponse passwordReset(RestorePasswordRequest request);

    @Path("/auth/user/password/confirm")
    @POST
    @Operation(summary = "Confirm User Password Reset",
            method = "POST",
            tags = {"auth"},
            description = "",
            parameters = {
                    @Parameter(name = "token",
                            description = "Password reset confirmation request",
                            required = true,
                            schema = @Schema(implementation = RestorePasswordConfirmRequest.class))
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    schema = @Schema(implementation = RestorePasswordConfirmResponse.class)
                            )),
                    @ApiResponse(responseCode = "400",
                            description = "Error",
                            content = @Content(
                                    schema = @Schema(implementation = IsFlawed.class)
                            ))
            })
    RestorePasswordConfirmResponse passwordConfirmReset(RestorePasswordConfirmRequest request);


}
