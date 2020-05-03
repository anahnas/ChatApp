$(document).ready(()=> {
	
	$('#login-form').submit((event)=>{
		event.preventDefault();
		
		let username=$('#username').val();
		let password=$('#password').val();
		
		console.log('username', username);
		console.log('password', password);
	
		$.post({
			url: 'rest/chat/login',
			data: JSON.stringify({"username":username, "password":password}),
			contentType: 'application/json',
			success: function(data) {
                sessionStorage.setItem('username', username);
				// localStorage.setItem("user", u.username);
				// localStorage.setItem("UO", JSON.stringify(u));
				window.location='./index.html';	
			},
			error: function() {
				alert('Error! You entered wrong username or password!');
			}
		});
		
	});
		
		
	
});