$(document).ready(()=> {
	
	$("#register-form").submit((event)=>{
		event.preventDefault();
		const username = $('#username').val();
		const password = $("#password").val();
		
		console.log('username', username);
		console.log('password', password);

		
		$.post({
			url: "rest/chat/register2",
			data: JSON.stringify({"username":username, "password":password}),
			contentType: "application/json",
			success: function(data) {
				sessionStorage.setItem('username', username);
				window.location.href='./login.html';
				console.log("User registered");
				
			},
			error: function(err) {
				console.log("Error");
				alert("Error: username already exists");
			}	
		});
		
	});
		
});