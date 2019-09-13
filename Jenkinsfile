pipeline {
    agent any
    stages {
        stage('Environment Preparation') {
            steps {
                echo 'Preparing environment for terraform execution'
                script {
                    env.EXEC_PATH = "application/"
                    if (params.TF_WORKSPACE == "plan") {
						env.TF_WORKSPACE = "planned"
					} else if (params.TF_WORKSPACE == "apply") {
						env.TF_WORKSPACE = "present"
					} else if (params.TF_WORKSPACE == "destroy"){
						env.TF_WORKSPACE = "absent"
					} else {
                        echo "Not a valid action"
                    }
                }
            }
        }
        stage('terraform plan') {
            steps {
                script {
                    dir("${WORKSPACE}/${env.EXEC_PATH}") {
                    echo 'terraform plan started'
                    sh """
                    ansible-playbook site.yml -e application=${params.APPLICATION} -e environment=${params.ENVIRONMENT} -e tf_workspace=${params.TF_WORKSPACE} -e tf_action=${params.TF_ACTION} -e tf_backend_provider=${params.TF_BACKEND_PROVIDER} -e s3_backend_bucket_region=${params.S3_BACKEND_BUCKET_REGION} -e s3_backend_bucket=${params.S3_BACKEND_BUCKET} -e s3_backend_dynamodb_table=${params.S3_BACKEND_DYNAMODB_TABLE} -e consul_backend_server_address=${params.CONSUL_BACKEND_SERVER_ADDRESS} -e consul_backend_server_scheme=${params.CONSUL_BACKEND_SERVER_SCHEME} -e consul_backend_kv_path=${params.CONSUL_BACKEND_KV_PATH}
                    """
                    }
                }
            }
        }
        stage('terraform apply') {
            when {
                expression { params.TERRAFORM_ACTION == "apply" }
            }
			steps {
                script {
                    dir("${WORKSPACE}/${env.EXEC_PATH}") {
                    input message: "Check terraform plan , if okay approve ?"
                    echo 'terraform apply operation'
                    sh """
                    ansible-playbook site.yml -e application=${params.APPLICATION} -e environment=${params.ENVIRONMENT} -e tf_workspace=${params.TF_WORKSPACE} -e tf_action=${params.TF_ACTION} -e tf_backend_provider=${params.TF_BACKEND_PROVIDER} -e s3_backend_bucket_region=${params.S3_BACKEND_BUCKET_REGION} -e s3_backend_bucket=${params.S3_BACKEND_BUCKET} -e s3_backend_dynamodb_table=${params.S3_BACKEND_DYNAMODB_TABLE} -e consul_backend_server_address=${params.CONSUL_BACKEND_SERVER_ADDRESS} -e consul_backend_server_scheme=${params.CONSUL_BACKEND_SERVER_SCHEME} -e consul_backend_kv_path=${params.CONSUL_BACKEND_KV_PATH}
                    """
                    }
                }
			}
		}
        stage('terraform apply') {
            when {
                expression { params.TERRAFORM_ACTION == "destroy" }
            }
			steps {
                script {
                    dir("${WORKSPACE}/${env.TERRAFORM_LOCATION}") {
                    input message: "Check terraform plan , if okay approve ?"
                    echo 'terraform destroy operation'
                    sh """
                    ansible-playbook site.yml -e application=${params.APPLICATION} -e environment=${params.ENVIRONMENT} -e tf_workspace=${params.TF_WORKSPACE} -e tf_action=${params.TF_ACTION} -e tf_backend_provider=${params.TF_BACKEND_PROVIDER} -e s3_backend_bucket_region=${params.S3_BACKEND_BUCKET_REGION} -e s3_backend_bucket=${params.S3_BACKEND_BUCKET} -e s3_backend_dynamodb_table=${params.S3_BACKEND_DYNAMODB_TABLE} -e consul_backend_server_address=${params.CONSUL_BACKEND_SERVER_ADDRESS} -e consul_backend_server_scheme=${params.CONSUL_BACKEND_SERVER_SCHEME} -e consul_backend_kv_path=${params.CONSUL_BACKEND_KV_PATH}
                    """
                    }
                }
			}
		}
	}
}