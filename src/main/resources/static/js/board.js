// ==  global variable == //
let submitButton;
let title;


// 회원가입 내용 json 형식으로 서버에 보내기
let index = {
    init: function() { // function()이 아닌 ()=> 쓴 이유 : this를 binding 하기 위해
            $("#button-board-save").on("click" , () => {
                this.save();
            });

            $("#button-board-update").on("click" , ()=> {
                this.update();
            });

            $("#btn-board-delete").on("click" , ()=> {
                this.deleteBoard();
            });
    },

    //== board save ==//
    save: function() {

        const newBoardData = {
            content : $("#summernote").summernote('code'),
            title : $("#title").val(),
            userId : $("#userId").val(),
        };

        $.ajax({
            url : `/boards/save`,
            type : 'POST',
            data : JSON.stringify(newBoardData), // data format -> json
            processData: false,
            contentType: "application/json; charset=utf-8", // http body data 형식
        }).done(function(res){ // javscript object로 변환하여 res에 담아줌

            console.log(res);
            alert("글 등록이 성공적으로 완료되었습니다!.");
            location.href="/";

        }).fail(function(error){
            console.log(error);
            alert("글 등록에 실패 했습니다.");
        });
    },

    //== board update ==//
    update: function() {

        const boardId = $("#board_id").val();

        const updatedBoardData = {
            content : $("#summernote").summernote('code'),
            title : $("#title").val(),
            userId : $("#userId").val(),
            boardId : boardId,
        };

        $.ajax({
            url : `/boards/`+ boardId + `/update`,
            type : 'PUT',
            data : JSON.stringify(updatedBoardData), // data format -> json
            processData: false,
            contentType: "application/json; charset=utf-8", // http body data 형식
            dataType : "text"
        }).done(function(res){ // javscript object로 변환하여 res에 담아줌

            console.log(res);
            alert("성공적으로 수정되었습니다!");
            location.href="/boards/" + boardId;

        }).fail(function(error){
            console.log(error);
            alert("글 수정에 실패 했습니다.");
            location.href="/boards/"+ boardId + "/update";
        });
    },

    //== board delete ==//
    deleteBoard: function(){

        const boardId = $("#boardId").val();
        const passwordData = {
            password : $("#password").val(),
        }

        $.ajax({
            url : `/boards/`+ boardId,
            type : 'DELETE',
            data : JSON.stringify(passwordData), // data format -> json
            processData: false,
            contentType: "application/json; charset=utf-8", // http body data 형식
            dataType : "text"
        }).done(function(res){ // javscript object로 변환하여 res에 담아줌

            console.log(res);
            alert("성공적으로 삭제되었습니다!");
            location.href="/";

        }).fail(function(jqXHR , error , textStatus){
            console.log(jqXHR.status)
            let errorMessage;
            let errorMessageContainer = $(".errorMessage-container");
            if(jqXHR.status === 400){

                // 이미 비밀번호 에러 메세지가 있을경우 기존의 메세지 사용 (새로 만들지 않음)
                if(errorMessageContainer.children().length > 0) return;
                // 처음 비밀번호 입력에 실패 했을 경우
                $("#password").val("");
                errorMessage = "<p class = 'alert alert-danger'>" + "비밀번호가 일치하지 않습니다 " + "</p>";
                errorMessageContainer.append(errorMessage);
            }

        });
    }
};


// textarea value가 없을 경우 버튼 disabled (default value)
$(document).ready(()=> {

    // init setting
    $('#summernote').summernote({
        tabsize : 2,
        height: 300,
        minHeight: 300,
        maxHeight: null,
        focus : true,
        lang : "ko-KR",
        placeholder: '내용을 입력해주세요'
    });


   // keyup event handler (button disabled setting)
   // summernote.keyup
   let title;
   let content;

   // keyup event function
   $('#summernote').on('summernote.keyup', ()=> {
        buttonDisabledAttrSetting(title ,removeInitialTagInSummernoteEditor(content));
   });
   $("#title").on("keyup" , ()=> {
        buttonDisabledAttrSetting(title ,removeInitialTagInSummernoteEditor(content));
   });

   // 게시글 삭제 버튼 disabled 유무
   let password;
   let deleteButton = $("#btn-board-delete");

    $("#password").on("keyup" , ()=> {
        password = $("#password").val();

        if(password.length != 0){
            deleteButton.attr("disabled" ,false);
            return;
        }

    // 닫기 버튼 눌렀을 때 모달 안에 입력된 내용 삭제
    $("#btn-close-delete").on("click", ()=> {
        document.getElementById("password").value="";
    });

    deleteButton.attr("disabled" , true);
   });
});



// button disabled 사용 유무 함수
function buttonDisabledAttrSetting (title , content) {
    title = $("#title").val();

    console.log(title.length , content.length);
    if(content.length != 0 && title.length != 0){
        $("#button-board-save , #button-board-update").attr("disabled" , false);
        return;
     }

     $("#button-board-save , #button-board-update").attr("disabled" , true);
};

function removeInitialTagInSummernoteEditor(content){
    content = $("#summernote").summernote("code");
    while(content.startsWith('<p><br></p>')){
    content = content.replace('<p><br></p>','')
    }

    while(content.endsWith('<p><br></p>')){
    content = content.replace(new RegExp('<p><br></p>$'),'')
    }

    return content;
}

$(document).ready(()=>{
    index.init();
});
