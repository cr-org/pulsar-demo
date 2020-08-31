{ packages ? import ./pkgs.nix { inherit compiler; }, compiler ? "ghc883" }:

let
  inherit (packages) pkgs hp supernova;
  pulsar-producer = hp.callCabal2nix "pulsar-producer" ./. { inherit supernova; };
in
  pkgs.haskell.lib.dontCheck pulsar-producer
