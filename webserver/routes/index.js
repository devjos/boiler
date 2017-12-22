var express = require('express');
var router = express.Router();
var path = require('path');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.sendFile(path.join(__dirname, '../public', 'index.html'));
});

router.post('/login', function(req, res, next) {
  var user = req.body.user;
	var pw = req.body.pw;

  if (user == 'admin' && pw == 'password'){
    req.session.user = 'admin';
    res.sendStatus(200);
  }
  else{
    res.sendStatus(403);
  }
})

router.get('/logout',function(req, res, next){
  req.session.destroy(function(err){
    if(err) console.log(err);
    else res.redirect('/');
  })
});

module.exports = router;
