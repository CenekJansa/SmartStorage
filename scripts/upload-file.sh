curl -v -X POST http://localhost:8080/graphql \
  -H "Content-Type: multipart/form-data" \
  -F operations='{
    "query": "mutation UploadDoc($id: Int!, $file: Upload!) { uploadDocument(sectionId: $id, file: $file) { success, attachmentId, errorMessage } }",
    "variables": {
      "id": 1,
      "file": null 
    }
  }' \
  -F map='{
    "0": ["variables.file"]
  }' \
  -F 0=@/home/chang/Documents/SecureStorage/documents/kupni-smlouva-auto.pdf