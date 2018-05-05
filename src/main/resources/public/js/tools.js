function setCookie(tokenContent, expires)
{
    var expireDate = new Date(expires);
    document.cookie = 'token=' + tokenContent + ';expires=' + expireDate;
}

function getCookieByName(name)
{
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function deleteCookie(name)
{
    document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

function displayErrorMessages (response)
{
    var jsonAsObject = JSON.parse(response.responseText);
    var errorMessages = jsonAsObject.errorMessages;
    var error= document.getElementById("errors");
    error.innerText = "";
    for(var i = 0; i < errorMessages.length; i++)
    {
        error.innerHTML += "<ul>" + errorMessages[i] + "</ul>";
    }
    error.classList.add("alert-danger");
}

function abmelden() //entweder onClick beim HTML-Button, der diese Funktion aufruft, oder https://api.jquery.com/click/ oder logout beim Backend aufrufen
{
    $.ajax({
        type: 'POST',
        url: domain + '/service/login/logout',
        contentType: "application/json;charset=utf-8",
        data:
        "{" +
        "\"token\":\"" + getCookieByName('token') + "\"" +
        "}",
        success: function (jsonVomServer) {
            window.location = domain + "/index.html";
            deleteCookie('token');
        },
        error: function (response) //Fehlerfall
        {
            console.log("Es gab einen Fehler beim Abmelden!");
            window.location = domain + "/index.html";
            deleteCookie('token');
        }
    });
}

function autologin() {
    var token = getCookieByName('token');
    if (token != null) {
        $.ajax({
            type: 'POST',
            url: domain + '/service/token/isTokenValid',
            contentType: "application/json;charset=utf-8",
            data:
            "{" +
            "\"token\":\"" + token + "\"" +
            "}",
            success: function (jsonVomServer)
            {
                window.location = domain + "/app/index.html";
            },
            error: function (response)
            {
            }
        });
    }
}

function autologoff() {
    var userToken = getCookieByName('token');
    $.ajax({
        type: 'POST',
        url: domain + '/service/token/isTokenValid',
        contentType: "application/json;charset=utf-8",
        data:
        "{" +
        "\"token\":\"" + userToken + "\"" +
        "}",
        success: function (jsonVomServer)
        {
        },
        error: function (response)
        {
            deleteCookie('token');
            window.location = domain;
        }
    });
}

function getDateAsLong(datum)
{
    var parts = datum.match(/(\d+)/g);
    return new Date(parts[2], parts[1] - 1, parts[0]).getTime();
}

function getZeitpunktAsLong(datumMitUhrzeit) {
    var parts = datumMitUhrzeit.match(/(\d+)/g);
    return new Date(parts[2] + "/" + parts[1] + "/" + parts[0] + " " + parts[3] + ":" + parts[4]).getTime();
}

/*function getHumanTimeByDateObject(dateObject) {
    if (dateObject.getDay() != NaN || dateObject.getMonth() != NaN || dateObject.getYear() != NaN) {
        var tag = dateObject.getDate();
        if(tag < 10) {
            tag = "0" + tag;
        }
        var monat = (dateObject.getMonth() + 1);
        if(monat < 10) {
            monat = "0" + monat;
        }
        return tag + "." + monat + "." + dateObject.getFullYear();
        //return dateObject.getUTCDate() + "." + (dateObject.getUTCMonth()+1) + "." + dateObject.getUTCFullYear();
    }
    console.log("Das Datum konnte nicht geladen werden.");
    return "";
}

//Kategorien in DropDown laden (unter Verwendung der Methoden changeData und ajaxGetCategories), Formatierung und Definition für Variablen
var categoryId1 = 0;
var categoryId2 = 0;
var categoryId3 = 0;   //EbeneId=Ebene+1
function loadEbene(optionElement, ebeneId) {
    var ebene = "";
    var parentVal=0;
    if (ebeneId === 1) //es gibt keinen Wert, ebeneId bedeutet onLoad;
    {
        parentVal = 0;
    } else { //lies den Wert aus dem option Element
        parentVal = optionElement.value;
    }

    //ebene bestimmt das DropDown-Menü, ob 1.,2. oder 3.; EbeneId ist das zu verändernde DropDown Menü
    if (ebeneId !== 4) {
        switch (ebeneId) {
            //int ebeneId soll zu String formatiert werden
            case 1:
                ebene = ".ebene1";
                break;
            case 2:
                ebene = ".ebene2"; //Es wurde ein Element aus der Dropdownliste der 1. Kategorie ausgewählt. Die 2. ebene ist zu füllen
                categoryId1=parentVal; //die categoryId wird geupdated.
                categoryId2=0;
                categoryId3=0;
                break;
            case 3:
                ebene = ".ebene3"; //siehe case 2
                categoryId2=parentVal;
                categoryId3=0;
                break;
        }
        if(parentVal!=0 ||ebeneId===1){ //wenn das Option Element nicht ein "wähle kategorie" oder wenn die sachen onload geladen weredn
            ajaxGetCategories(parentVal, ebene, ebeneId); //führe die ajax request aus und fülle die ebene
        }else{//Wenn der Nutzer ein "Wähle Kategorie" element ausgewählt hat (value=0), dann leere die nachfolgenden Ebenen
            emptyCategoryFrom(ebeneId);
        }
    }else {
        categoryId3=parentVal; //wegen der Logik musste die categoryId definition hier extern für die letzte Ebene gesetzt werden.
    }
}

function emptyCategoryFrom(ebeneId){ //Die Ebenen, mit der hier angegeben Id werden geleert und mit dem platzhalter gefüllt.
    switch(ebeneId){
        case 2://2 und 3 leeren
            $('.ebene2').empty();
            $('.ebene2').append($('<option>',
                {
                    text: "Wähle Kategorie",
                    value: 0
                }));
        //da kein break, läuft er automatisch in 3 rein.
        case 3: //3 leeren
            $('.ebene3').empty();
            $('.ebene3').append($('<option>',
                {
                    text: "Wähle Kategorie",
                    value: 0
                }));
            break;
    }
}

//Füllt die DropDowns
//ebene ist beinhaltet referenz auf die klasse .ebeneX die unter der aufgerufenen Ebene liegt , ebeneId ist die derzeitige Ebene+1
function changeData(data, ebene, EbeneId)
{
    categoryEbene = EbeneId;
    $(ebene).empty();   //leert die DropDowns
    if (ebene === ".ebene2"){
        emptyCategoryFrom(3);
    }
    $(ebene).append($('<option>',
        {
            text: "Wähle Kategorie",
            value: 0
        }));
    for (var i = 0; i < data.length; i++)    //Füllt die DropDowns mit den Kategorien (in data gespeichert)
    {
        $(ebene).append($('<option>',
            {
                value: data[i].categoryId,
                text: b64DecodeUnicode(data[i].description),
            }))
    }

}

//Anfrage an Backend durch parentVal (KategorieId von ausgewählter Option), um alle Unterkategorien zu erhalten
//ebene ist beinhaltet referenz auf die klasse .ebeneX  , ebeneId ist die derzeitige Ebene+1
function ajaxGetCategories(parentVal, ebene, EbeneId) {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/service/activity/getCategories?parent=' + parentVal, //Parameter namens Parent wird mit Inhalt "parentVal" übergeben
        contentType: "application/json",
        dataType: 'json',
        date: {
            parent: parentVal,
        },
        success: function (data)
        {
            changeData(data, ebene, EbeneId);
        },
        error: function ()
        {
            console.log("Fehler bei Aufruf von 'getCategories' mit Parameter parent = " + parentVal); //TODO: SweerAlert
        }
    })
}

function getCategoryId() //gibt die CategoryId zurück, welche gültig ist (nicht 0)
{
    var category=0;
    if(categoryId1!=0){
        category=categoryId1;
        if(categoryId2!=0){
            category=categoryId2;
            if(categoryId3!=0){
                category=categoryId3;
            }
        }
    }
    return category;
}

function datePickerTimeToLong(timeStamp)
{
    // 23.03.2018 12:41
    // 04 Dec 1995 00:12:00 GMT
    //console.log(timeStamp);

    var splitted = timeStamp.split(".");
    if(splitted.length<2){
        return 0;
    }
    var formatted = splitted[0] + " ";
    var tmp;
    switch (splitted[1]) {
        case "01":
            tmp = "JAN";
            break;
        case "02":
            tmp = "FEB";
            break;
        case "03":
            tmp = "MAR";
            break;
        case "04":
            tmp = "APR";
            break;
        case "05":
            tmp = "MAY";
            break;
        case "06":
            tmp = "JUN";
            break;
        case "07":
            tmp = "JUG";
            break;
        case "08":
            tmp = "AUG";
            break;
        case "09":
            tmp = "SEPT";
            break;
        case "10":
            tmp = "OCT";
            break;
        case "11":
            tmp = "NOV";
            break;
        case "12":
            tmp = "DEC";
            break;
    }
    formatted += tmp + " ";
    formatted += splitted[2].split(" ")[0] + " " + splitted[2].split(" ")[1] + ":00 GMT";   //first get a long of the Date
    formatted = Date.parse(formatted);  //this is the Date without recognition of timeZones
    formatted += 60000 * new Date().getTimezoneOffset();    //long time in regard of Timezone
    return formatted    //Date.parse transforms from local time to gmt long time.
}

function longTimeToDatePickerTime(longTime)
{
    //Eingabe ist Long. Date.toLocaleString macht: 18.3.2018, 16:47:00
    // wir wollen: 18.03.2018, 16:47
    var vagueArray = new Date(longTime).toLocaleString().split(","); //==first
    var preciseArray = vagueArray[0].split(".");
    var formatted;
    if (preciseArray[0].length < 2)     //Tag //das format soll immer 03.xxx anstatt 3.xxx sein
    {
        formatted = "0" + preciseArray[0] + ".";
    } else {
        formatted = preciseArray[0] + ".";
    }
    if (preciseArray[1].length < 2)     //Monat
    {
        formatted += "0" + preciseArray[1] + ".";
    } else {
        formatted += preciseArray[1] + ".";
    }
    formatted += preciseArray[2]; //add year
    preciseArray = vagueArray[1].split(":");
    formatted += preciseArray[0] + ":" + preciseArray[1];
    return formatted;
}

function getUrlParameter(sParam)     //read Url Parameter
{
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++)
    {
        sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] === sParam)
        {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}

function anzeigenInitiieren(activityId)
{
    window.location.href ='../app/showActivity.html?activityId='+activityId;
}

function anzeigenBearbeiten(activityId)
{
    window.location.href ='../app/editactivity.html?activityId='+activityId;
}

function b64EncodeUnicode(str) { //from string to byteArray
    return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function(match, p1) {
        return String.fromCharCode(parseInt(p1, 16))
    }))
}

function b64DecodeUnicode(str) { //from byteArray to String
    return decodeURIComponent(Array.prototype.map.call(atob(str), function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
    }).join(''))
}
function profilAnsichtBeenden(activityId){
    if(activityId !==undefined && activityId!== null){
        window.location.href ='../app/showActivity.html?activityId='+activityId;
    } else {
        window.location.href = document.referrer;
    }
}*/