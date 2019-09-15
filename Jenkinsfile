pipeline {
    agent any
    stages {
        stage('Environment Preparation') {
            steps {
                echo 'Preparing environment for terraform execution'
                script {
                    env.EXEC_PATH = "artifacts/"
                    if (params.TF_ACTION == "plan") {
						env.TF_ACTION = "planned"
					} else if (params.TF_ACTION == "apply") {
						env.TF_ACTION = "present"
					} else if (params.TF_ACTION == "destroy"){
						env.TF_ACTION = "absent"
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
                    ansible-playbook site.yml -e tf_application=${params.APPLICATION} -e tf_environment=${params.ENVIRONMENT} -e tf_workspace=${params.TF_WORKSPACE} -e tf_action=planned -e tf_backend_provider=${params.TF_BACKEND_PROVIDER} -e s3_backend_bucket_region=${params.S3_BACKEND_BUCKET_REGION} -e s3_backend_bucket=${params.S3_BACKEND_BUCKET} -e s3_backend_dynamodb_table=${params.S3_BACKEND_DYNAMODB_TABLE} -e consul_backend_server_address=${params.CONSUL_BACKEND_SERVER_ADDRESS} -e consul_backend_server_scheme=${params.CONSUL_BACKEND_SERVER_SCHEME} -e consul_backend_kv_path=${params.CONSUL_BACKEND_KV_PATH}
                    """
                    }
                }
            }
        }
        stage('terraform apply') {
            when {
                expression { params.TF_ACTION == "apply" }
            }
			steps {
                script {
                    dir("${WORKSPACE}/${env.EXEC_PATH}") {
                    input message: "Check terraform plan , if okay approve ?"
                    echo 'terraform apply operation'
                    sh """
                    ansible-playbook site.yml -e tf_application=${params.APPLICATION} -e tf_environment=${params.ENVIRONMENT} -e tf_workspace=${params.TF_WORKSPACE} -e tf_action=${env.TF_ACTION} -e tf_backend_provider=${params.TF_BACKEND_PROVIDER} -e s3_backend_bucket_region=${params.S3_BACKEND_BUCKET_REGION} -e s3_backend_bucket=${params.S3_BACKEND_BUCKET} -e s3_backend_dynamodb_table=${params.S3_BACKEND_DYNAMODB_TABLE} -e consul_backend_server_address=${params.CONSUL_BACKEND_SERVER_ADDRESS} -e consul_backend_server_scheme=${params.CONSUL_BACKEND_SERVER_SCHEME} -e consul_backend_kv_path=${params.CONSUL_BACKEND_KV_PATH}
                    """
                    }
                }
			}
		}
        stage('terraform destroy') {
            when {
                expression { params.TF_ACTION == "destroy" }
            }
			steps {
                script {
                    dir("${WORKSPACE}/${env.EXEC_PATH}") {
                    input message: "Note: Will delete whole stack , if okay approve ?"
                    echo 'terraform destroy operation against stack'
                    sh """
                    ansible-playbook site.yml -e tf_application=${params.APPLICATION} -e tf_environment=${params.ENVIRONMENT} -e tf_workspace=${params.TF_WORKSPACE} -e tf_action=${env.TF_ACTION} -e tf_backend_provider=${params.TF_BACKEND_PROVIDER} -e s3_backend_bucket_region=${params.S3_BACKEND_BUCKET_REGION} -e s3_backend_bucket=${params.S3_BACKEND_BUCKET} -e s3_backend_dynamodb_table=${params.S3_BACKEND_DYNAMODB_TABLE} -e consul_backend_server_address=${params.CONSUL_BACKEND_SERVER_ADDRESS} -e consul_backend_server_scheme=${params.CONSUL_BACKEND_SERVER_SCHEME} -e consul_backend_kv_path=${params.CONSUL_BACKEND_KV_PATH}
                    """
                    }
                }
			}
		}
	}
}
