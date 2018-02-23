
    function modalConfirmation(link, text)
    {
        document.getElementById('confirmation_message').innerHTML = text;
        $("#confirmationDialog").dialog({
            buttons : {
                "Yes" : function() {
                    $(this).dialog("close");
                    window.location = link.href;
                },
                "No" : function() {
                    $(this).dialog("close");
                }
            }
        });
        $("#confirmationDialog").dialog("open");
    }

    function modalForm( text, title)
    {
        document.getElementById('info_message').innerHTML = text;
        document.getElementById('infoDialog').title = title;
        $("#infoDialog").dialog({
            buttons : {
                "OK" : function() {
                    $(this).dialog("close");
                }
            }
        });
        $("#infoDialog").dialog("open");
    }
