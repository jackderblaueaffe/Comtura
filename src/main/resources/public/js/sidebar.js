$("#toggleSidebar").click(function (e) {
    var x = document.getElementById("toggleSidebar");
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
    x.classList.toggle("change");
});