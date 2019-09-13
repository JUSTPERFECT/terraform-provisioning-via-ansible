variable "region" {
  default     = "us-east-1"
  description = "region in which to deploy application"
}

variable "environment" {
  description = "environment to deploy the script"
  default     = "dev"
}

variable "bucket_name" {
  default     = "flux7-demo-bucket"
  description = "bucket to create for project"
}

variable "application" {
  description = "application name"
  default     = "app1"
}
