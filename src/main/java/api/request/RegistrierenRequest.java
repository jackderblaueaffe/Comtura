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
public class RegistrierenRequest {

    @Size(min = 1, message = "Der Benutzername muss vorhanden sein.")
    @NotNull(message = "Der Benutzername muss vorhanden sein.")
    private String benutzername;

    @Size(min = 1, message = "Das 1. Passwort muss vorhanden sein.")
    @NotNull(message = "Das 1. Passwort muss vorhanden sein.")
    private String passwort1;

    @Size(min = 1, message = "Das 2. Passwort muss vorhanden sein.")
    @NotNull(message = "Das 2. Passwort muss vorhanden sein.")
    private String passwort2;

}
