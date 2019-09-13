variable "region" {
  default     = "us-east-1"
  description = "region in which to deploy application"
}

variable "docker_image_version" {
  description = "docker image version to use"
  default = "latest"
}

variable "environment" {
  description = "environment to deploy the script"
  default     = "dev"
}

variable "application" {
  description = "application name"
  default     = "app2"
}