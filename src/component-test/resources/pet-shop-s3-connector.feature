Feature: Pet Shop S3 Connector
  Scenario: fetch a pre-signed link to upload a file
    Given I need a pre-signed link to "upload" a file "my.txt" using bucket "bucket"
    When I make a request for the link
    Then the pre-signed link should be successfully returned

  Scenario: fetch a pre-signed link to download a file
    Given I need a pre-signed link to "download" a file "my.txt" using bucket "bucket"
    When I make a request for the link
    Then the pre-signed link should be successfully returned

  Scenario: using the pre-signed links
    Given I need a pre-signed link to "upload" a file "my.txt" using bucket "bucket"
    And I use the link to "upload" a file "my.txt" into a bucket "bucket"
    And I need a pre-signed link to "download" a file "my.txt" using bucket "bucket"
    When I use the link to "download" a file "my.txt" into a bucket "bucket"
#    TODO then for verify file contents / check file exists
