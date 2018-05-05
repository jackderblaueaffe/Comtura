package api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangePasswortRequest {

    @NotNull(message = "Token ist leer.")
    private String token;

    @Size(min = 1, message = "Das 1. Passwort muss vorhanden sein.")
    @NotNull(message = "Das 1. Passwort muss vorhanden sein.")
    private String passwort1;

    @Size(min = 1, message = "Das 2. Passwort muss vorhanden sein.")
    @NotNull(message = "Das 2. Passwort muss vorhanden sein.")
    private String passwort2;

}
