package argos.com.br.crud_basico.application.dto.auth;

public record LoginResponse(String acessToken, String TokenType) {
    public static LoginResponse bearer(String token){
        return new LoginResponse(token, "Bearer");
    }
}
