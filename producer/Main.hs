{-# LANGUAGE DeriveAnyClass, DeriveGeneric, OverloadedStrings #-}

module Main where

import           Control.Concurrent             ( threadDelay )
import           Control.Monad                  ( forever )
import           Data.Aeson
import           Data.Foldable                  ( traverse_ )
import           Data.Text                      ( Text )
import           GHC.Generics                   ( Generic )
import           Pulsar

main :: IO ()
main = runPulsar resources $ \(Producer produce) ->
  let sendMessages = traverse_ produce messages
  in  sleep 3 >> sendMessages

data Msg = Msg
  { name :: Text
  , year :: Int
  } deriving (Generic, FromJSON, ToJSON, Show)

messages :: [PulsarMessage]
messages =
  let msg = [Msg "fromHaskell" 2020, Msg "toScala" 2021]
  in  PulsarMessage . encode <$> msg

resources :: Pulsar (Producer IO)
resources = do
  ctx <- connect defaultConnectData
  newProducer ctx topic

sleep :: Int -> IO ()
sleep n = threadDelay (n * 1000000)
