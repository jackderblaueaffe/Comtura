function passwortAendern() {

    var passwortKlartext = document.getElementById("neuesPasswort").value;
    var passwortKlartext2 = document.getElementById("neuesPasswort2").value;
    $.get({
        url: domain + "/service/login/getPublicKey",
        success: function (jsonVomServer) {
            var encrypt = new JSEncrypt();
            encrypt.setPublicKey(jsonVomServer.encoded);
            var verschluesseltesPasswort = encrypt.encrypt(passwortKlartext);
            var verschluesseltesPasswort2 = encrypt.encrypt(passwortKlartext2);
            $.ajax({
                type: 'POST',
                url: domain + '/service/benutzer/changePasswort',
                contentType: "application/json",
                data:
                "{" +
                "\"token\":\"" + getCookieByName("token") + "\"," +
                "\"passwort1\":\"" + verschluesseltesPasswort + "\"," +
                "\"passwort2\":\"" + verschluesseltesPasswort2 + "\"" +
                "}",
                success: function (jsonVomServer) //Anfrage erfolgreich (HTTP Statuscode 200)
                {
                    $('.errors').empty();
                    swal({
                        title: "Du hast erfolgreich dein Passwort geändert!",
                        type: "success"
                    }).then(function () {
                        // Redirect the user
                        window.location = domain + "/app/index.html";
                    });
                },
                error: function (response) //Fehlerfall
                {
                    displayErrorMessages(response);
                }
            });
        },
        error: function (response) {
            console.log("Fehler bei der Ermittlung des Public Keys"); //TODO: sollte noch weggemacht werden
        }
    });
}