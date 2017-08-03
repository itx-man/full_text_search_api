var http = require('http'); // Basic HTTP functionality
var path = require('path'); // Parse directory paths
var express = require('express'); // Provide static routing to pages
var port = 8080;
var app = setupExpress();

//---------------------------------------------------------------------------------------------------

function setupExpress()
{
	// HTML files located here
	var viewsDir = path.join(__dirname, 'views');

	// This becomes the root directory used by the HTML files
	var publicDir = path.join(__dirname, 'public');

	var app = express();
	app.use(express.static(publicDir));

  app.get('/', function(req, res)
  {
    res.sendFile('views/error.html', { root: '.' });
  });

	// Get the index page (only option)
	app.get('/webapp', function(req, res)
	{
		res.sendFile('views/index.html', { root: '.' });
	});

	app.get('/search/reviews', function(req, resp)
	{
		console.log(req.url);
		var options = {
			host: 'localhost',
			port: 9000,
			path: req.url,
			method: 'GET',
			headers: {
					'Content-Type': 'application/json'
			}
		};

		var dreq = http.get(options, function(res) {
			var bodyChunks = [];
			res.on('data', function(chunk) {
				bodyChunks.push(chunk);
			}).on('end', function() {
				var body = Buffer.concat(bodyChunks);
				resp.writeHead(200, {"Content-Type" : "json"});
				resp.write(body);
				resp.end();
			})
		});

		dreq.on('error', function(e) {
			console.log('ERROR: ' + e.message);
			resp.writeHead(200, {"Content-Type" : "text/html"});
			resp.write("Error");
			resp.end();
		});
	});

	// Handle any misc errors by redirecting to a simple page and logging
	app.use(function(err, req, res, next)
	{
		console.log(err.stack);
		res.status(err.status || 500);

		res.sendFile('/views/error.html', { root: '.' });
	});

	return app;
}

var server = http.Server(app);
server.listen(port, function()
{
		// Use long polling, else all responses are async
});
