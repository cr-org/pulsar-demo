{ compiler ? "ghc883" }:

let
  pkgs = import (
    builtins.fetchTarball {
      name   = "nixos-unstable-2020-08-15";
      url    = "https://github.com/NixOS/nixpkgs-channels/archive/96745f022835.tar.gz";
      sha256 = "1jfiaib3h6gmffwsg7d434di74x5v5pbwfifqw3l1mcisxijqm3s";
    }
  ) {};

  supernovaPkg =
    builtins.fetchTarball {
      name   = "supernova-v0.0.2";
      url    = "https://github.com/cr-org/supernova/archive/da948bb.tar.gz";
      sha256 = "1w6rxhnijwckw3wyzqddiyc1p90sk2wzq9qnl5zg0cw4djkxs46h";
    };

  newSupernova = pkgs.callPackage supernovaPkg {};

  hp = pkgs.haskell.packages.${compiler}.override {
    overrides = newPkgs: oldPkgs: rec {
      supernova = newSupernova;
    };
  };
in
{
  pkgs = pkgs;
  hp = hp;
  supernova = newSupernova;
}
