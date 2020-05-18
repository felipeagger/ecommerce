// Code generated by qtc from "signup.html". DO NOT EDIT.
// See https://github.com/valyala/quicktemplate for details.

//line templates/index/signup.html:1
package index

//line templates/index/signup.html:1
import (
	"github.com/valyala/fasthttp"
)

//line templates/index/signup.html:4
import (
	qtio422016 "io"

	qt422016 "github.com/valyala/quicktemplate"
)

//line templates/index/signup.html:4
var (
	_ = qtio422016.Copy
	_ = qt422016.AcquireByteBuffer
)

//line templates/index/signup.html:5
type SignupPage struct {
	CTX *fasthttp.RequestCtx
}

//line templates/index/signup.html:10
func (p *SignupPage) StreamCSS(qw422016 *qt422016.Writer) {
//line templates/index/signup.html:10
	qw422016.N().S(` `)
//line templates/index/signup.html:10
}

//line templates/index/signup.html:10
func (p *SignupPage) WriteCSS(qq422016 qtio422016.Writer) {
//line templates/index/signup.html:10
	qw422016 := qt422016.AcquireWriter(qq422016)
//line templates/index/signup.html:10
	p.StreamCSS(qw422016)
//line templates/index/signup.html:10
	qt422016.ReleaseWriter(qw422016)
//line templates/index/signup.html:10
}

//line templates/index/signup.html:10
func (p *SignupPage) CSS() string {
//line templates/index/signup.html:10
	qb422016 := qt422016.AcquireByteBuffer()
//line templates/index/signup.html:10
	p.WriteCSS(qb422016)
//line templates/index/signup.html:10
	qs422016 := string(qb422016.B)
//line templates/index/signup.html:10
	qt422016.ReleaseByteBuffer(qb422016)
//line templates/index/signup.html:10
	return qs422016
//line templates/index/signup.html:10
}

//line templates/index/signup.html:12
func (p *SignupPage) StreamTitle(qw422016 *qt422016.Writer) {
//line templates/index/signup.html:12
	qw422016.N().S(` SignUp `)
//line templates/index/signup.html:12
}

//line templates/index/signup.html:12
func (p *SignupPage) WriteTitle(qq422016 qtio422016.Writer) {
//line templates/index/signup.html:12
	qw422016 := qt422016.AcquireWriter(qq422016)
//line templates/index/signup.html:12
	p.StreamTitle(qw422016)
//line templates/index/signup.html:12
	qt422016.ReleaseWriter(qw422016)
//line templates/index/signup.html:12
}

//line templates/index/signup.html:12
func (p *SignupPage) Title() string {
//line templates/index/signup.html:12
	qb422016 := qt422016.AcquireByteBuffer()
//line templates/index/signup.html:12
	p.WriteTitle(qb422016)
//line templates/index/signup.html:12
	qs422016 := string(qb422016.B)
//line templates/index/signup.html:12
	qt422016.ReleaseByteBuffer(qb422016)
//line templates/index/signup.html:12
	return qs422016
//line templates/index/signup.html:12
}

//line templates/index/signup.html:15
func (p *SignupPage) StreamBody(qw422016 *qt422016.Writer) {
//line templates/index/signup.html:15
	qw422016.N().S(`

<nav class="navbar navbar-inverse">
<div class="container-fluid">
<div class="navbar-header">
    <a class="navbar-brand" href="http://ecommerce/">E-Commerce</a>
</div>
<ul class="nav navbar-nav">
    <li class="active"><a href="http://ecommerce/">Home</a></li>
</ul>

<form class="navbar-form navbar-left" action="http://ecommerce/catalog" method="GET">
    <div class="form-group"> 
    <input type="text" class="form-control" placeholder="Search" style="width:370px;" name="search">
    </div>  
    <button id="btn-view" type="submit" class="btn btn-default" style="width:100px" value="Search">
    <span class="glyphicon glyphicon-search"></span> Search
    </button>
</form> 

<ul class="nav navbar-nav navbar-right">
    <li><a href="http://ecommerce/orders/"><span class="glyphicon glyphicon-align-justify"></span> Orders</a></li>
    <li><a href="http://ecommerce/cart/"><span class="glyphicon glyphicon-shopping-cart"></span> Cart</a></li>
    <li><a href="http://ecommerce/login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
    <li><a href="http://ecommerce/signup"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
</ul>
</div>
</nav>



<h1 id="title">Account</h1>

<div class="container">  

<div class="center">

    <form id="login-form" class="form" action="http://ecommerce/signup" method="POST" onsubmit="return signup()">

    <h3 style="text-align: center">Register</h3>

    <div class="form-group" style="width: 400px;">
        <label for="username" class="text-info">Name</label><br>
        <input type="text" style="width: 400px; margin: 0 auto;" name="username" id="username" class="form-control" placeholder="Name">
    </div>
    <div class="form-group" style="width: 400px;">
        <label for="email" class="text-info">E-mail</label><br>
        <input type="text" style="width: 400px; margin: 0 auto;" name="email" id="email" class="form-control" placeholder="E-Mail">
    </div>
    <div class="form-group" style="width: 400px;">
        <label for="password" class="text-info">Password</label><br>
        <input type="password" style="width: 400px; margin: 0 auto;" name="password" id="password" class="form-control" placeholder="Password">
    </div>
    <div class="form-group" style="width: 400px;">
        <input style="width: 90px; margin-left: 160px;" class="btn btn-info btn-md" value="SignUp" onclick="signup()" readonly>
        <br>
        <a href="http://ecommerce/login" class="text-info" style="text-align: right">Login here</a>
    </div>

    <div id="success" class="alert alert-success" role="alert" style="visibility: hidden">
        <h3 id="msgs">
    </div>

    <div id="fail" class="alert alert-danger" role="alert" style="visibility: hidden">
        <h3 id="msgf">
    </div>

    </form>

</div>

</div>

<script>

function validateForm() {
  var name = document.getElementById('username').value;
  var username = document.getElementById('email').value;
  var password = document.getElementById('password').value;

  if (name.trim() == "" || username.trim() == "" || password.trim() == "") {
    alert("Name, E-Mail and password is required");
    return false;
  }

  return true;
}

function signup(){

    if (validateForm()){

        const frmdata = new FormData();
        var username = document.getElementById('username').value;
        frmdata.append('name', username);
        frmdata.append('username', document.getElementById('email').value);
        frmdata.append('password', document.getElementById('password').value);

        $.ajax({
            url: 'http://ecommerce/signup',
            processData: false,
            contentType: false,
            cache: false,
            method: 'POST',
            dataType: 'json',
            data: frmdata,
            success: function(data) {
                
                document.getElementById("msgs").innerHTML = "SignUp Successful"
                document.getElementById("success").style.visibility = "visible";
                document.getElementById("fail").style.visibility = "hidden";

                window.location.replace("http://ecommerce/login");
            },
            error: function(data) {
                document.getElementById("msgf").innerHTML = "Error on Register! :("
                document.getElementById("fail").style.visibility = "visible";
                document.getElementById("success").style.visibility = "hidden";

                if (data["responseJSON"]["Error"]){
                    document.getElementById("msgf").innerHTML = data["responseJSON"]["Error"];
                }

                console.log("Error:");
                console.log(data);
            }
        });

    }
}
    
</script>

`)
//line templates/index/signup.html:148
}

//line templates/index/signup.html:148
func (p *SignupPage) WriteBody(qq422016 qtio422016.Writer) {
//line templates/index/signup.html:148
	qw422016 := qt422016.AcquireWriter(qq422016)
//line templates/index/signup.html:148
	p.StreamBody(qw422016)
//line templates/index/signup.html:148
	qt422016.ReleaseWriter(qw422016)
//line templates/index/signup.html:148
}

//line templates/index/signup.html:148
func (p *SignupPage) Body() string {
//line templates/index/signup.html:148
	qb422016 := qt422016.AcquireByteBuffer()
//line templates/index/signup.html:148
	p.WriteBody(qb422016)
//line templates/index/signup.html:148
	qs422016 := string(qb422016.B)
//line templates/index/signup.html:148
	qt422016.ReleaseByteBuffer(qb422016)
//line templates/index/signup.html:148
	return qs422016
//line templates/index/signup.html:148
}

//line templates/index/signup.html:151
func (p *SignupPage) StreamJS(qw422016 *qt422016.Writer) {
//line templates/index/signup.html:151
	qw422016.N().S(` `)
//line templates/index/signup.html:151
}

//line templates/index/signup.html:151
func (p *SignupPage) WriteJS(qq422016 qtio422016.Writer) {
//line templates/index/signup.html:151
	qw422016 := qt422016.AcquireWriter(qq422016)
//line templates/index/signup.html:151
	p.StreamJS(qw422016)
//line templates/index/signup.html:151
	qt422016.ReleaseWriter(qw422016)
//line templates/index/signup.html:151
}

//line templates/index/signup.html:151
func (p *SignupPage) JS() string {
//line templates/index/signup.html:151
	qb422016 := qt422016.AcquireByteBuffer()
//line templates/index/signup.html:151
	p.WriteJS(qb422016)
//line templates/index/signup.html:151
	qs422016 := string(qb422016.B)
//line templates/index/signup.html:151
	qt422016.ReleaseByteBuffer(qb422016)
//line templates/index/signup.html:151
	return qs422016
//line templates/index/signup.html:151
}