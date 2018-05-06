function registrieren() {

    var benutzername = document.getElementById("benutzername").value;
    var passwortKlartext1 = document.getElementById("passwort").value;
    var passwortKlartext2 = document.getElementById("passwort2").value;
    //Aufruf per GET, um Public Key des Servers zu ermitteln
    $.get({
        url: domain + "/service/login/getPublicKey",
        dataType: 'json',
        success: function (jsonVomServer) {
            var encrypt = new JSEncrypt();
            encrypt.setPublicKey(jsonVomServer.encoded);
            var verschluesseltesPasswort1 = encrypt.encrypt(passwortKlartext1);
            var verschluesseltesPasswort2 = encrypt.encrypt(passwortKlartext2);
            $.ajax({
                type: 'POST',
                url: domain + '/service/login/registrieren',
                contentType: "application/json",
                data: //Daten, die zum Server geschickt werden
                "{" +
                "\"benutzername\":\"" + benutzername + "\"," +
                "\"passwort1\":\"" + verschluesseltesPasswort1 + "\"," +
                "\"passwort2\":\"" + verschluesseltesPasswort2 + "\"" +
                "}",
                dataType: 'json',
                success: function (jsonVomServer) //Anfrage erfolgreich (HTTP Statuscode 200)
                {
                    //Token auslesen
                    var token = jsonVomServer["tokenContent"];
                    var gueltigBis = jsonVomServer["ablaufdatum"];
                    setCookie(token, gueltigBis);

                    //Redirect auf die Startseite
                    window.location = domain + "/app/index.html";
                },
                error: function (response) //Fehlerfall
                {
                    displayErrorMessages(response);
                }
            });
        },
        error: function (response) {
            console.log("Fehler bei der Ermittlung des Public Keys");
        }
    });


}