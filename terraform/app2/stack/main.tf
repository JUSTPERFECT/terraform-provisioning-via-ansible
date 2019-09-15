# Create a container
resource "docker_container" "docker" {
  image = "${docker_image.ubuntu.latest}"
  name  = "${var.application}-${var.environment}-flux-demo"
}

resource "docker_image" "ubuntu" {
  name = "docker.io/ubuntu:${var.docker_image_version}"
}
