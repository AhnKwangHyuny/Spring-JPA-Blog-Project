// 회원가입 내용 json 형식으로 서버에 보내기
let index = {
    init: function() { // function()이 아닌 ()=> 쓴 이유 : this를 binding 하기 위해
            $("#button-save").on("click" , () => {
                this.save();
            });
    },
    save: function() {

        const newUserData = {
            username : $("#username").val(),
            email : $("#email").val(),
            password : $("#password").val()
        };

        // ajax호춯시 default가 비동기 호출
        // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!!
        $.ajax({
            url : `/auth/join`,
            type : 'POST',
            data : JSON.stringify(newUserData), // data format -> json
            processData: false,
            contentType: "application/json; charset=utf-8", // http body data 형식
            dataType: "json" // 요청을 서버로 보내 응답이 왔을 때 받을 data 형식 (default형식은 string) //
        }).done(function(res){ // javscript object로 변환하여 res에 담아줌

            console.log(res);
            alert("회원가입이 완료되었습니다.");
            location.href="/blog";

        }).fail(function(){
            alert("회원가입에 실패했습니다.");
            location.href="/user/join";
        });
    }
};

// 회원가입 버튼 클릭
index.init();
