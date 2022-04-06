// Global Variables
let cropper;

// 현제 페이지 redirect 시

// 회원가입 내용 json 형식으로 서버에 보내기
let index = {
    init: function() { // function()이 아닌 ()=> 쓴 이유 : this를 binding 하기 위해
            $("#button-save").on("click" , () => {
                this.save();
            });

            $("#button-password-update").on("click" , ()=> {
                this.checkPwd();
            });
    },
    save: function() {

        // joinform data 반환
        let form = $("#joinForm")[0];

        // 데이터 전송을 위한 폼 데이터 포멧 선언
        let formData = new FormData(form);

        // formdata -> json 형식으로 전환
        let object = {};
        formData.forEach((value, key) => object[key] = value);
        let json = JSON.stringify(object);

        console.log(json);

        // ajax호춯시 default가 비동기 호출
        // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!!
        $.ajax({
            url : `/auth/join`,
            type : 'POST',
            data : json, // data format -> json
            processData: false,
            contentType: 'application/json; charset=utf-8',
            dataType : "json"
        }).done(function(res){ // javscript object로 변환하여 res에 담아줌

            console.log(res);
            alert("회원가입이 완료되었습니다.");
            location.href="/";

        }).fail(function(error){
            console.log(error);
            alert("회원가입에 실패했습니다.");
//            location.href="/auth/join";
        });
    },

    checkPwd : function(){
        // 입력받은 유저 비밀번호
        const password = $("#password").val();

        // 유저 아이디
        const userId = $("#userId").val();

        // 요청 하기 위한 json 객체 생성
        const obj = {"password" : password};
        jsonData = JSON.stringify(obj);

        $.ajax({
            url : `/users/` + userId + `/checkPwd`,
            type : 'POST',
            data : jsonData,
            processData: false,
            contentType: 'application/json; charset=utf-8',
            dataType : "json",
            async : false, // : ajax 다중 호출 시 순서대로 처리하기 위해 async 옵션을 false로 설정한다.
        }).done(function(res){
            // 성공적으로 패스워드 검증 로직이 끝났을 경우 200 반환
            if(res["statusCode"] == 200){
                // 새 비밀번호 변경 로직
                console.log("checkPwd : success!");
                index.modifyPwd();
            }

        }).fail(function(error){
            console.log(error);
            let res = error["responseJSON"];

            if(res["statusCode"] == 400) {
                // 에러 메세지 담은 p태그
                let errorMessage = createErrorMessage(res["data"]);

                // errorMessage 넣을 첫번째 password 폼 그룹 가져오기
                let formGroup = $(".form-group").first();

                formGroup.append(errorMessage);
            }
        });
    },

    modifyPwd : function(){
        let newPwd = $("#new-password").val();
        let rePwd = $("#re-password").val();

        // newPwd 와 rePwd 입력 값이 같은지 검증
        if(validateNewPassword(newPwd , rePwd)){
            let userId = $("#userId").val();
            doAJAX_StorePWD(userId , newPwd); // 패스워드 변경 요청 ajax 함수 실행
        }

        // newPwd , rePwd 두 값이 다를 경우 오류 메세지 호출 후 리턴
        let formGroups = $(".form-group:gt(0)"); // form-group 에서 2, 3번째 그룹 가져옴 (첫번째는 password 입력란)
        let errorMessage = createErrorMessage("두 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        let errorMessageTag = stringToHtml(errorMessage);

        // error message 첫번째를 제외한 모든 form-group에 적용

        formGroups.append(errorMessageTag);

        return;
    },
};


/*
    user profile Image cropper
*/

$(document).ready(()=> {
/*image upload modal*/
    $('#filePhoto').change(event => {
        // jquery object have not files property ...
        //  take files property from event target object
        let input = $(event.target);
        if(input[0].files && input[0].files[0]){

            let reader = new FileReader();

            // when successfully file was loaded executes onload handler
            reader.onload = (e) => {
                $('#imageUploadButton').removeAttr('disabled');
                let image = document.getElementById('imagePreview');
                image.src = e.target.result;

                if(cropper !== undefined){
                    // Destroy the cropper and remove the instance from the image.
                    // destroy method is a cropper method
                    cropper.destroy();
                }

                cropper = new Cropper(image , {
                    aspectRatio : 3/4,
                    background : false,
                    zoomOnWheel: false
                });

            }
            /*The readAsDataURL method is used to read the contents of
            the specified Blob or File. When the read operation is finished,
            the readyState becomes DONE, and the loadend is triggered. At that time,
            the result attribute contains the data as a data: URL representing the file's data as a base64 encoded string.*/
            reader.readAsDataURL(input[0].files[0]);
        }
    });

    // user profile image upload logic
    $("#imageUploadButton").on('click' , ()=> {
        let canvas = cropper.getCroppedCanvas();
        // Upload cropped image to server if the browser supports `HTMLCanvasElement.toBlob`.
        // The default value for the second parameter of `toBlob` is 'image/png', change it if necessary.

        if(canvas == null || canvas == undefined ) {
            alert('Could not upload Image. Make sure it is an image file.');
            return;
        }

//        // join page에 선택한 이미지 샘플 보여지도록 하는 함수
//        canvas.toBlob(function(blob) {
//        	var reader = new FileReader();
//        	reader.onload = function(e) {
//        		document.getElementById('user_profile_image_sample').src = reader.result;
//        		document.getElementById('modal_close_button').click();
//        	}
//        	// reader 가 blob 파일 reading 후 onload 이벤트가 트리거 됨
//        	reader.readAsDataURL(blob);
//        });

        canvas.toBlob( blob => {

            let userId = $("#userId").val();

            const formData = new FormData();
            formData.append("croppedImage" , blob);

            $.ajax({
                url : `/files/userImage/` + userId,
                type : 'POST',
                data : formData,
                processData: false,
                contentType: false,
            }).done(function(res){ // javscript object로 변환하여 res에 담아줌

              console.log(res);
              alert("성공적으로 프로필 사진을 등록했습니다!");
//              location.href="/";

            }).fail(function(error){
              console.log(error);
              alert("회원가입에 실패했습니다.");
  //            location.href="/auth/join";
            });
        });
    });
});

/*
     password 변경 로직
*/

$(document).ready(()=> {
    // button 활성화 로직 (입력란에 하나라도 빈칸 있을 경우 disabled 유지)
    $("#password , #new-password , #re-password").on('keyup' , (event) => {
        let valueList = [$("#password").val() , $("#new-password").val() , $("#re-password").val()];
        console.log(valueList);
        let button = $("#button-password-update");
        // 비밀번호 입력 란에 공백이 하나라도 있으면 변경 버튼 disabled
        for(idx in valueList){
            value = valueList[idx];
            if(value.trim() == "") {
               button.prop("disabled" , true);
               return;
            }
        }

        // 입력란 모두 채워져 있다면 변경 disabled 속성 제거
        button.prop("disabled" , false);
        return;
    });

});



// 회원가입 버튼 클릭
index.init();

/*
   함수모음
*/

// 메세지 p태그 생성 함수
function createErrorMessage(errorMessage){
    return `<p class = "errorMessage">${errorMessage}</p>`;
}

// 새로운 패스워드와 재 입력한 패스워가 일치하는 검증하는 함수
function validateNewPassword(newPwd , rePwd){
    if(newPwd == rePwd){
        return true;
    }
    else {
        return false;
    }
}

// 변경할 비밀번호 저장 요청 ajax
function doAJAX_StorePWD(userId , newPwd) {

    let object = {"newPassword" : newPwd};
    json = JSON.stringify(object);

    $.ajax({
        url : `/users/` + userId + `/modifyPwd` ,
        type : 'PUT',
        data : json,
        processData: false,
        contentType: 'application/json; charset=utf-8',
        dataType : "json"
    }).done(function(res){
        console.log(res);

        if(res["statusCode"] == 204) { // update statusCode 200 or 204(recommend)
            alert("성공적으로 비밀번호를 변경하였습니다.");
            location.href="/users/" + userId;
            return;
        }
        // 정상적인 수행이 되지 않았을 경우
        console.log(res);
        alert("알 수 없는 이유로 변경에 실패했습니다.");
        return;

    }).fail(function(error){
        console.log(error);
        // 실패 로직

        // 비밀번호 변경 후 1일 경과되지 않았는데 다시 비밀번호 변경 요철 했을때


        return;
    });
}

// convert plain string to html
function stringToHtml(str){
    let parser = new DOMParser();
    let doc = parser.parseFromString(str , "text/html");
    return doc.body;
}