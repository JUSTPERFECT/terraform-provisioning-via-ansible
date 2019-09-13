pipelineJob("terraform-execution-from-jenkins-via-ansible") {
	parameters {
		activeChoiceParam('APPLICATION') {
            description('Select the application stack to build')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''return ['app1', 'app2']''')
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
		stringParam('S3_BACKEND_BUCKET_REGION', 'default', 'select s3 state file bucket region in case of AWS backend')
		stringParam('S3_BACKEND_BUCKET', 'default', 'select s3 state file bucket in case of AWS backend')
		stringParam('S3_BACKEND_DYNAMODB_TABLE', 'default', 'select dynamodb table to lock state in case of AWS backend')
		stringParam('CONSUL_BACKEND_SERVER_ADDRESS', 'default', 'select consul server address in case of consul backend')
		stringParam('CONSUL_BACKEND_SERVER_SCHEME', 'default', 'select consul server protocol/scheme in case of consul backend')
		stringParam('CONSUL_BACKEND_KV_PATH', 'default', 'select consul server kv store path in case of consul backend')
        activeChoiceReactiveReferenceParam('DEPLOY_INFO') {
            description('Deployement Information')
            choiceType('FORMATTED_HTML')
            groovyScript {
                script('''
				try {
				def application= COMPONENT
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
				return data
				''')
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
						branches('$job_branch')
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