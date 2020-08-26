let
  config = {
    packageOverrides = pkgs: rec {
      sbt = pkgs.sbt.overrideAttrs (
        old: rec {
          version = "1.3.12";

          patchPhase = ''
            echo -java-home ${pkgs.openjdk11} >> conf/sbtopts
          '';
        }
      );
    };
  };

  pkgs = import (
    builtins.fetchTarball {
      name   = "nixos-unstable-2020-08-15";
      url    = "https://github.com/NixOS/nixpkgs-channels/archive/96745f022835.tar.gz";
      sha256 = "1jfiaib3h6gmffwsg7d434di74x5v5pbwfifqw3l1mcisxijqm3s";
    }
  ) { inherit config; };
in
  pkgs.mkShell {
    buildInputs = with pkgs; [
      openjdk11
      sbt
    ];
  }
