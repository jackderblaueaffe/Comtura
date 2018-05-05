package repository.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Benutzer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id;

    @Column(unique = true)
    @Size(min = 1, message = "Bitte geben Sie einen Benutzernamen ein.")
    @NotNull(message = "Bitte geben Sie einen Benutzernamen ein.")
    private String benutzername;

    @Size(min = 1, message = "Bitte geben Sie ein Passwort ein.")
    @NotNull(message = "Bitte geben Sie ein Passwort ein.")
    private String passwort;

    private int fehlgeschlageneLoginversuche;

    private long gesperrtBis;

    //orphanRemoval = true: wenn Benutzer gelöscht wird, wird auch Token gelöscht
    @OneToOne(mappedBy = "benutzer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Token token;

    //TODO: Berechtigungslevel?

}
