package repository.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Token implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id;

    @Column(unique=true)
    @NotNull(message = "Der TokenContent ist leer.")
    private String tokenContent;

    @NotNull(message = "Das Ablaufdatum des Tokens ist nicht gesetzt.")
    private long ablaufdatum;

    @NotNull(message = "Der Token benoetigt einen Schlüssel.")
    private byte[] encryptionKey;

    //Beziehung zu Benutzer
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name= "id")
    @NotNull(message = "Der Token muss einem Benutzer zugeordnet sein.")
    private Benutzer benutzer;

}
