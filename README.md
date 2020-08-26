pulsar-demo
===========

Integration of a producer written in Haskell (using the [supernova](https://github.com/cr-org/supernova) library) and a consumer written in Scala (using the [neutron](https://github.com/cr-org/neutron) library), both created and maintained by ChatRoulette.

### Run it

Start a Pulsar instance using Docker.

```
$ docker run -it \
  -p 6650:6650 \
  -p 8080:8080 \
  --mount source=pulsardata,target=/pulsar/data \
  --mount source=pulsarconf,target=/pulsar/conf \
  apachepulsar/pulsar:2.5.1 \
  bin/pulsar standalone
```

Run the consumer.

```
cd consumer
nix-shell --run "sbt run"
```

[![consumer](https://asciinema.org/a/xc068c7UlpQyIuBYYHorZkzHs.png)](https://asciinema.org/a/xc068c7UlpQyIuBYYHorZkzHs)

Run the producer.

```
cd producer
nix-shell --run "cabal new-run"
```

[![producer](https://asciinema.org/a/j9NiQeylYtG7ncHYXWM6CPZOp.png)](https://asciinema.org/a/j9NiQeylYtG7ncHYXWM6CPZOp)
