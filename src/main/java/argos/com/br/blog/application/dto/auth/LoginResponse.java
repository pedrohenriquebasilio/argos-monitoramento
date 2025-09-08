package argos.com.br.blog.application.dto.auth;

public record LoginResponse(String acessToken, String TokenType) {
    public static LoginResponse bearer(String token){
        return new LoginResponse(token, "Bearer");
    }
}
