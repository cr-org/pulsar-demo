pulsar-demo
===========

Integration of a producer written in Haskell (using the [supernova](https://github.com/cr-org/supernova) library) and a consumer written in Scala (using the [neutron](https://github.com/cr-org/neutron) library), both created and maintained by ChatRoulette.

### Run it

Start a Pulsar instance using `docker-compose`.

```shell
docker-compose up
```

Run the consumer.

```shell
cd consumer
nix-shell --run "sbt run"
```

Run the producer.

```shell
cd producer
nix-shell --run "cabal new-run"
```
