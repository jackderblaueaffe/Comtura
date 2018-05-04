function login() {
    var benutzername = document.getElementById("benutzername").value;
    var passwort = document.getElementById("passwort").value;

    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/service/login/login',
        contentType: "application/json",
        data: "{\"benutzername\":\"" + benutzername + "\",\"passwort\":\"" + passwort + "\"}", //Daten, die zum Server geschickt werden
        dataType: 'json',
        success: function(jsonVomServer) //Anfrage erfolgreich (HTTP Statuscode 200)
        {
            //Token auslesen
            var token = jsonVomServer["userToken"];
            var benutzerId = jsonVomServer["benutzerId"];
            var gueltigBis = jsonVomServer["ablaufdatum"];
            setCookie(token, gueltigBis);
            //Redirect auf die Startseite
            window.location = "http://localhost:8080/app/";
        },
        error: function(response) //Fehlerfall
        {
            var jsonAsObject = JSON.parse(response.responseText);
            var ul = document.getElementById("errors");
            ul.innerHTML = "<li>" + jsonAsObject.fehlermeldung + "</li>";
            ul.classList.add("alert-danger");
        }
    });
}