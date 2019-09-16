terraform {
  backend "s3" {}
}

resource "docker_container" "nginx-server" {
  name  = "${var.application}-${var.environment}-flux-demo"
  image = "${docker_image.nginx.latest}"

  ports {
    internal = 80
  }

  volumes {
    container_path = "/usr/share/nginx/html"
    host_path      = "/var/lib/nginx"
    read_only      = true
  }
}

resource "docker_image" "nginx" {
  name = "docker.io/nginx:${var.docker_image_version}"
}
