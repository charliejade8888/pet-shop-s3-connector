Feature: Pet Shop S3 Connector

  Scenario: fetch a pre-signed link to upload a file
    Given I need a pre-signed link to "upload" a file "my-image.png" into a bucket "bucket":
    When I make a request for the link
    Then the pre-signed link should be successfully returned

  Scenario: fetch a pre-signed link to download a file
    Given I need a pre-signed link to "download" a file "my-image.png" into a bucket "bucket":
    When I make a request for the link
    Then the pre-signed link should be successfully returned
