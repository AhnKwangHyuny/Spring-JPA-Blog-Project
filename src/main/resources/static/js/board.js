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
            })
    },
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
//                location.href="/boards/"+ boardId + "/update";
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
