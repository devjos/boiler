var srv = require('../bin/www');
var chai = require('chai');
var request = require('supertest');

var expect = chai.expect;

describe('boiler web server', function () {

  describe('/', function () {
    it('should return 200', function (done) {
      request(srv)
        .get('/')
        .expect(200, done);
    });
  })

  describe('/login', function(){
    it('should allow the login', function (done) {
      login(function(header){
        done();
      });
    });
  })

  describe('/settings', function(){
    it('should return the settings', function (done) {
      request(srv)
        .get('/settings')
        .expect(200, done);
    });
    it('should set the settings', function (done) {
      login(function (cookie){
        setTemperatures(65, 23, 76);
        setTemperatures(cookie, 40, 43, 15, done);
      });

    });
    it('should not allow illegal values', function (done) {
      login(function (cookie){
        request(srv)
          .post('/settings')
          .set('Cookie', cookie)
          .send({"TARGET_TEMPERATURE":0,"LEGIONELLEN_TEMPERATURE":0,"EMPTY_TEMPERATURE":100})
          .expect(400, done);
      });
    });
    it('should not allow when a value is missing', function (done) {
      login(function (cookie){
        request(srv)
          .post('/settings')
          .set('Cookie', cookie)
          .send({"LEGIONELLEN_TEMPERATURE":30,"EMPTY_TEMPERATURE":30})
          .expect(400, done);
      });
    });
  })


  it('404 everything else', function (done) {
    request(srv)
      .get('/foo/bar')
      .expect(404, done);
  });

  function setTemperatures(cookie, target, legionellen, empty, done){
    request(srv)
      .post('/settings')
      .set('Cookie', cookie)
      .send({"TARGET_TEMPERATURE":target,"LEGIONELLEN_TEMPERATURE":legionellen,"EMPTY_TEMPERATURE":empty})
      .expect(200)
      .end( function(){
        request(srv)
          .get('/settings')
          .expect('{"TARGET_TEMPERATURE":' + target + ',"LEGIONELLEN_TEMPERATURE":' + legionellen + ',"EMPTY_TEMPERATURE":' + empty + '}')
          .expect(200, done);
      });
  };

  function login(cb){
    request(srv)
      .post('/login')
      .send({user:'admin', pw: 'password'})
      .end(function(err, res) {
        expect(res.statusCode).to.be.equal(200);
        cb(res.headers['set-cookie'])
      });
  }

})

srv.close();
