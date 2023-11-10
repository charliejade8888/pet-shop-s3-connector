aws --endpoint-url=http://localhost:4566 --region=us-east-1 s3api list-buckets
aws --endpoint-url=http://localhost:4566 --region=us-east-1 s3api --bucket create-bucket mybucket

config:
  aws:
    region: us-east-1
    s3:
      url: http://127.0.0.1:4566
      bucket-name: bucket
      access-key: localstack
      secret-key: localstack

https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html#getting-started-install-instructions

bucket set-up here - https://medium.com/javarevisited/upload-and-retrieve-files-form-aws-s3-using-the-presigned-url-pattern-in-java-ecde26e9441f