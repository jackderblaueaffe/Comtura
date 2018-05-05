function login() {
    var benutzername = document.getElementById("benutzername").value;
    var passwortKlartext = document.getElementById("passwort").value;
    //Aufruf per GET, um Public Key des Servers zu ermitteln
    $.get({
        url: domain + "/service/login/getPublicKey",
        dataType: 'json',
        success: function (jsonVomServer) {
            var encrypt = new JSEncrypt();
            encrypt.setPublicKey(jsonVomServer.encoded);
            var verschluesseltesPasswort = encrypt.encrypt(passwortKlartext);
            //Aufruf der Login-Methode des Backends per POST
            $.ajax({
                type: 'POST',
                url: domain + '/service/login/login',
                contentType: "application/json",
                data: "{\"benutzername\":\"" + benutzername + "\",\"passwort\":\"" + verschluesseltesPasswort + "\"}", //Daten, die zum Server geschickt werden
                dataType: 'json',
                success: function(jsonVomServer) //Anfrage erfolgreich (HTTP Statuscode 200)
                {
                    //Token auslesen
                    var token = jsonVomServer["tokenContent"];
                    var benutzerId = jsonVomServer["benutzerId"];
                    var gueltigBis = jsonVomServer["ablaufdatum"];
                    setCookie(token, gueltigBis);
                    //Redirect auf die Startseite
                    window.location = domain + "/app/index.html";
                },
                error: function(response) //Fehlerfall
                {
                    console.log(response);
                    var jsonAsObject = JSON.parse(response.responseText);
                    var ul = document.getElementById("errors");
                    ul.innerHTML = "<li>" + jsonAsObject.fehlermeldung + "</li>";
                    ul.classList.add("alert-danger");
                }
            });
        },
        error: function (response) {
            console.log("Fehler bei der Ermittlung des Public Keys");
        }
    });
}