resource "aws_s3_bucket" "dummy_bucket" {
  bucket = "${var.application}-${var.environment}-${var.bucket_name}"
  acl    = "private"

  tags = {
    Application = "${var.application}"
    Environment = "${var.environment}"
  }
}
