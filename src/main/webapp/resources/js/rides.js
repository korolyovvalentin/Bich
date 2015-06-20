function addPoint() {
    var selectedCities = getSelectedCities();
    var availableCities = getAvailableCities(selectedCities);
    var $newFormGroup = $('<div class="form-group"></div>');
    var $newSelect = $('<select class="form-control" style="float: left; width: calc(100% - 32px)"></select>');
    $newSelect.attr('name', 'cities[' + selectedCities.length + '].id');
    $newFormGroup.append($newSelect);

    var $deleteBtn = $('<a href="#!" class="remove-button" title="Delete Point"></a>');
    $deleteBtn.append('<img style="margin-top: 5px" src="https://cdn3.iconfinder.com/data/icons/google-material-design-icons/48/ic_highlight_remove_48px-32.png" />')
    $deleteBtn.click(function () {
        $newFormGroup.remove();
    });
    $newFormGroup.append($deleteBtn);

    for (var i = 0, length = availableCities.length; i < length; i++) {
        $newSelect.append('<option value="' + availableCities[i].id + '">' + availableCities[i].name + '</option>')
    }

    $newFormGroup.insertBefore(this);
}

var getAvailableCities = function (selected) {
    var available = [];
    var token = $("[name='_csrf']").val();

    $.ajax({
        url: "/administration/cities/available",
        type: 'POST',
        dataType: 'json',
        async: false,
        headers: {
            'X-CSRF-Token': token,
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(selected),
        contentType: 'application/json',
        mimeType: 'application/json',
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                console.log(data[i].name + ' ' + data[i].id);
                available.push(data[i]);
            }
        },
        error: function (data, status, er) {
            alert("error: " + data + " status: " + status + " er:" + er);
        }
    });

    return available;
};

var getSelectedCities = function () {
    var selected = [];
    $('select.form-control').each(function () {
        var text = $(this).children("option:selected").text();
        selected.push({id: $(this).val(), name: text});
    });
    return selected;
};
