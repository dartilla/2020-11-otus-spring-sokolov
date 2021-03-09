function buildScriptRow(script) {
    id = script.id;
    return '<tr id="scriptRow-' + script.id + '">' +
        '<td>' + script.id + '</td>' +
        '<td>' + script.title + '</td>' +
        '<td>' + script.author.name + '</td>' +
        '<td><a href="javascript:void(0)" class="scriptLink" onclick="editScript(\'' + script.id + '\')">Edit</a></td>' +
        '<td><a href="javascript:void(0)" class="scriptLink" onclick="deleteScript(\'' + script.id + '\')">Delete</a></td>' +
        '</tr>'
}

function editScript(id) {
    clearScriptForm();
    $.ajax({
        url: "/rest/script/" + id,
        type: "GET",
        success: function (script) {
            $("#scriptRow-" + id).replaceWith(buildScriptRow(script));
            $(".scriptLink-" + id).hide();

            let genresStr = "";
            for (genre of script.genres) {
                if (genresStr !== "") genresStr = genresStr + ","
                genresStr = genresStr + genre.name;
            }
            $("#scriptRow-" + id).after(
                '<tr class="scriptFormRow""><td colspan="5">' + buildScriptForm("submitEditScriptForm", script, genresStr) + '</td></tr>'
            );
        }
    })
}

function buildAddScriptForm() {
    clearScriptForm();
    $("#addScriptLink").after(buildScriptForm("submitAddScriptForm", null, ""));
}

function orBlank(obj) {
    return (typeof obj == 'undefined') ? "" : obj;
}

function buildScriptForm(submitFuncName, script, genresStr) {
    let hideIdOrNotStyle = (typeof script?.id == 'undefined') ? 'style="display:none"' : "";
    return '<form id="scriptForm" onSubmit="return ' + submitFuncName + '()">' +
        ' <div ' + hideIdOrNotStyle + '><label>ID:</label><input type="text" readOnly="readonly" name="id" value="' + orBlank(script?.id) + '"/></div>' +
        ' <div><label>Title:</label><input type="text" name="title" value="' + orBlank(script?.title) + '"/></div>' +
        ' <div><label>Author:</label><input type="text" name="authorName" value="' + orBlank(script?.author?.name) + '"/></div>' +
        ' <div><label>Genres:</label><input type="text" name="genreNames" value="' + orBlank(genresStr) + '"/></div>' +
        ' <div><button type="submit">Save</button><button type="reset" onClick="clearScriptForm()">Cancel</button><span id="scriptFormErrorLabel"/></div>' +
        '</form>';
}

function submitEditScriptForm() {
    let data = getFormData($("#scriptForm"));
    $.ajax({
        url: '/rest/script/',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (script) {
            clearScriptForm();
            $("#scriptRow-" + id).replaceWith(buildScriptRow(script));
        },
        error: function (answer) {
            $("#scriptFormErrorLabel").text(answer.responseText)
        }
    });
    return false;
}

function submitAddScriptForm() {
    let data = getFormData($("#scriptForm"));
    $.ajax({
        url: '/rest/script/',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (script) {
            clearScriptForm();
            $('#scriptTableBody').append(buildScriptRow(script));
        },
        error: function (answer) {
            $("#scriptFormErrorLabel").text(answer.responseText)
        }
    });
    return false;
}

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });
    return indexed_array;
}

function clearScriptForm() {
    $(".scriptLink").show();
    $(".scriptFormRow").remove();
    $("#scriptForm").remove();
}

function deleteScript(id) {
    $.ajax({
        url: "/rest/script/" + id,
        type: "DELETE",
        success: function () {
            clearScriptForm();
            $("#scriptRow-" + id).remove();
        }
    })
}
