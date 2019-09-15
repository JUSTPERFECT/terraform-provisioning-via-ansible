def repo="https://github.com/JUSTPERFECT/terraform-provisioning-via-ansible.git"

pipelineJob("terraform-execution-from-jenkins-via-ansible") {
	parameters {
		stringParam('PIPELINE_BRANCH', 'master', 'branch to use for Jekinsfile')
		activeChoiceParam('APPLICATION') {
            description('Select the application stack to build')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''return ['app1','app2']''')
                fallbackScript('')
            }
        }
		activeChoiceReactiveParam('ENVIRONMENT') {
            description('Select the environment to build')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''
					if (APPLICATION.equals("app1")) {
						return ['dev', 'qa', 'stg', 'prod']
					} else if (APPLICATION.equals("app2")) {
						return ['dev', 'qa', 'prod']
					} else {
						return ['application name is not matching']
					}
					''')
                fallbackScript('')
            }
            referencedParameter('APPLICATION')
        }
		stringParam('TF_WORKSPACE', 'default', 'select the workspace')
        activeChoiceParam('TF_ACTION') {
            description('Select the terraform action to do')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''return ['plan', 'apply', 'destroy']''')
                fallbackScript('')
            }
        }
		activeChoiceParam('TF_BACKEND_PROVIDER') {
            description('Select the terraform backend provider to use')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''return ['local', 'aws', 'consul']''')
                fallbackScript('')
            }
        }
		textParam('S3_BACKEND_OPTIONS', '''S3_BACKEND_BUCKET_REGION="<bucket_region_here>"
S3_BACKEND_BUCKET="<bucket_name_here>"
S3_BACKEND_DYNAMODB_TABLE="<dynamodb_table_name_here>"''', 's3 bucket, region and dynamodb table for statefile backend configuration. Only fill incase of AWS backend')
		textParam('CONSUL_BACKEND_OPTIONS', '''CONSUL_BACKEND_SERVER_ADDRESS="<consul_server_address_here>"
CONSUL_BACKEND_SERVER_SCHEME="<consul_server_scheme_here>"
CONSUL_BACKEND_KV_PATH="<consul_kv_path_here>"''', 'consul server,scheme and key value path for statefile backend configuration. Only fill incase of consul backend')
        activeChoiceReactiveReferenceParam('DEPLOY_INFO') {
            description('Deployement Information')
            choiceType('FORMATTED_HTML')
            groovyScript {
                script('''try {
def application= APPLICATION
def environment = ENVIRONMENT
def backend = TF_BACKEND_PROVIDER
def workspace = TF_WORKSPACE
def action = TF_ACTION
	data ="""
	<html>
		<body>
			<table border = "1">
				<tr>
					<th>application</th>
					<th>environment</th>
					<th>backend provider</th>
					<th>selected workspace</th>
					<th>action</th>
				</tr>
				<tr>
					<td>${application}</td>
					<td>${environment}</td>
					<td>${backend}</td>
					<td>${workspace}</td>
					<td>${action}</td>
				</tr>      
			</table>
		</body>
	</html>
	"""
} catch (Exception e) {
	println(e)
}
return data''')
            fallbackScript('')
            }
			referencedParameter('APPLICATION')
			referencedParameter('ENVIRONMENT')
			referencedParameter('TF_WORKSPACE')
			referencedParameter('TF_ACTION')
			referencedParameter('TF_BACKEND_PROVIDER')
        }
	}
	definition {
		cpsScm {
			scm {
				git {
					remote {
						url(repo)
						branches('$PIPELINE_BRANCH')
						credentials('flux7')
						extensions {
							relativeTargetDirectory('artifacts')
						}
					}
				}
			}
			lightweight(false)
			scriptPath("artifacts/Jenkinsfile")
		}
	}
}