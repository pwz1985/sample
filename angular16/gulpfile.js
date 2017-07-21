var gulp = require('gulp');
var webserver = require('gulp-webserver');

gulp.task('default', function() {
  gulp.src('app')
    .pipe(webserver({
      // livereload: true,
      directoryListing: true,
      open: true,
      port: 80
    }));
});
