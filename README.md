## Meteor Shower ☆彡
[![CircleCI](https://circleci.com/gh/pine/meteorshower-server.svg?style=shield&circle-token=9d1800880fb0dd23fb7098d8b66e401f30d5a2c7)](https://circleci.com/gh/pine/meteorshower-server)

:star2: Do you remember all the repositories saw in the past?
<br>
<br>

## Requirements

- Java 11
- MySQL
- Redis

## Libraries

- Spring Boot 2.x
- MyBatis

## Getting started

```
$ ./scripts/setup_mysql.sh
$ ./gradlew :app:bootRun
```

## Deployment

```sh
$ heroku apps:create your-app
$ heroku plugins:install java
$ heroku config:set SPRING_PROFILES_ACTIVE=prod
$ heroku config:set TZ=Asia/Tokyo

$ heroku addons:create jawsdb:kitefin
$ heroku config | fgrep JAWSDB_URL

$ heroku addons:create heroku-redis:hobby-dev
$ heroku config | fgrep REDIS_URL

$ vim app/src/main/resources/application-prod.yml
$ ./gradlew :app:assemble
$ heroku deploy:jar --jar app/build/libs/app.jar --jdk 11
```

## Development
## JDK
For macOS users.

```
$ brew tap adoptopenjdk/openjdk
$ brew cask install adoptopenjdk11
```

## How to run server on local machine?

```sh
# Edit your local configure
$ cp app/src/main/resources/application-sample.yml app/src/main/resources/application-local.yml
$ vim app/src/main/resources/application-local.yml

# Run HTTP server
$ ./gradlew :app:bootRun
```

## See also

- [meteorshower-app](https://github.com/pine/meteorshower-app)

## License
MIT &copy; [Pine Mizune](https://profile.pine.moe)
