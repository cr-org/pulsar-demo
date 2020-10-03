{ compiler ? "ghc884" }:

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
      name   = "supernova-v0.0.3";
      url    = "https://github.com/cr-org/supernova/archive/e54ab43.tar.gz";
      sha256 = "1vwl26nkqwfyxmirvpk8j20cnla8dqy8gvvwaxjyxb9pacgw7yix";
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
