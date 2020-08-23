pulsar-demo
===========

Integration of a producer written in Haskell (using the [supernova](https://github.com/cr-org/supernova) library) and a consumer written in Scala (using the [neutron](https://github.com/cr-org/neutron) library), both created and maintained by ChatRoulette.

### Run it

Start a Pulsar instance using Docker:

```
$ docker run -it \
  -p 6650:6650 \
  -p 8080:8080 \
  --mount source=pulsardata,target=/pulsar/data \
  --mount source=pulsarconf,target=/pulsar/conf \
  apachepulsar/pulsar:2.5.1 \
  bin/pulsar standalone
```

Run the consumer:

```
nix-shell --run "cd consumer && bloop run root"
```

Run the producer:

```
nix-shell --run "cd producer && cabal new-run"
```
