{-# LANGUAGE DeriveAnyClass, DeriveGeneric, OverloadedStrings #-}

module Main where

import           Control.Concurrent             ( threadDelay )
import           Control.Monad.IO.Class         ( liftIO )
import           Data.Aeson
import           Data.Foldable                  ( traverse_ )
import           Data.Text                      ( Text )
import           GHC.Generics                   ( Generic )
import           Pulsar

data Msg = Msg
  { name :: Text
  , year :: Int
  } deriving (Generic, FromJSON, ToJSON, Show)

topic :: Topic
topic = defaultTopic "demo"

main :: IO ()
main = runPulsar (connect defaultConnectData) $ do
  (Producer send) <- newProducer topic
  liftIO $ traverse_ send messages >> sleep 1

messages :: [PulsarMessage]
messages =
  let msg = [Msg "fromHaskell" 2020, Msg "toScala" 2021]
  in  PulsarMessage . encode <$> msg

sleep :: Int -> IO ()
sleep n = threadDelay (n * 1000000)
