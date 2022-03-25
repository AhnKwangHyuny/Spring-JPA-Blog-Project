// Global Variables
let cropper;

// 회원가입 내용 json 형식으로 서버에 보내기
//let index = {
//    init: function() { // function()이 아닌 ()=> 쓴 이유 : this를 binding 하기 위해
//            $("#button-save").on("click" , () => {
//                this.save();
//            });
//    },
//    save: function() {
//
//        const newUserData = {
//            username : $("#username").val(),
//            email : $("#email").val(),
//            password : $("#password").val()
//        };
//
//        // ajax호춯시 default가 비동기 호출
//        // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!!
//        $.ajax({
//            url : `/auth/join`,
//            type : 'POST',
//            data : JSON.stringify(newUserData), // data format -> json
//            processData: false,
//            contentType: "application/json; charset=utf-8", // http body data 형식
//        }).done(function(res){ // javscript object로 변환하여 res에 담아줌
//
//            console.log(res);
//            alert("회원가입이 완료되었습니다.");
//            location.href="/";
//
//        }).fail(function(error){
//            console.log(error);
//            alert("회원가입에 실패했습니다.");
////            location.href="/auth/join";
//        });
//    }
//};


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
                    aspectRatio : 1/1,
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

        canvas.toBlob( blob => {
            console.log(blob);
            const imageFormData = new FormData();

            // Pass the image file name as the third parameter if necessary.
            imageFormData.append('croppedImage', blob);

            $.ajax({
                url : `/app/users/profilePicture`,
                type : 'POST',
                data : imageFormData,
                processData: false,
                contentType: false,
                success() {
                    console.log('Upload success');
                    location.reload();
                },
                error() {
                console.log('Upload error');
                }
            });
        });
    });
});



// 회원가입 버튼 클릭
index.init();
