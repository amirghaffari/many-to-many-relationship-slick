
    function modalForm( title, body)
    {
        document.getElementById('title_text').innerHTML = title;
        document.getElementById('body_text').innerHTML = body;
    }

    $(document).ready(function(){
        $(".infoModal").click(function(){
            modalForm( this.getAttribute("data-modalTitle"), this.getAttribute("data-modalBody"))
        });

        $("#loginBtn").click(function(){
            $("#loginForm").submit()
        });
    });

