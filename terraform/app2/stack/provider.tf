provider "aws" {
  version = "~> 2.0"
  region  = "${var.region}"
}

provider "docker" {
  host = "tcp://127.0.0.1:2376/"
}
