// AI Document Search ( Future feature... )
// BY ABDELILAH EL GUENNOUNI

class DocumentAIAssistant {
  constructor(openAiApiKey) {
    if (!openAiApiKey) {
      throw new Error("OpenAI API key is required.");
    }
    this.apiKey = openAiApiKey;
  }

  base64ToBlob(base64, mimeType = "application/pdf") {
    const byteCharacters = atob(base64);
    const byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += 512) {
      const slice = byteCharacters.slice(offset, offset + 512);
      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      byteArrays.push(new Uint8Array(byteNumbers));
    }

    return new Blob(byteArrays, { type: mimeType });
  }

  async extractTextFromPDF(pdfBlob) {
    const arrayBuffer = await pdfBlob.arrayBuffer();
    const pdfDocument = await pdfjsLib.getDocument({ data: arrayBuffer }).promise;
    const { numPages } = pdfDocument;

    const pagePromises = Array.from({ length: numPages }, async (_, i) => {
      const page = await pdfDocument.getPage(i + 1);
      const content = await page.getTextContent();
      const pageText = content.items.map(item => item.str).join(" ");
      return `\n--- Page ${i + 1} ---\n${pageText}`;
    });

    const pages = await Promise.all(pagePromises);
    return pages.join("");
  }

  async processDocuments(documents) {
    const textExtractionPromises = documents.map(async (doc) => {
      const blob = this.base64ToBlob(doc.base64);
      const textContent = await this.extractTextFromPDF(blob);
      return `${doc.name}:\n${textContent}`;
    });

    const extractedTexts = await Promise.all(textExtractionPromises);
    return extractedTexts.join("\n\n");
  }

  async askQuestion(documents, question) {
    const documentContext = await this.processDocuments(documents);

    const systemPrompt = "You are an intelligent assistant that helps users find information within their documents.";
    const userPrompt = `Using the provided documents below, please answer the user's question.
                        Be professional and mention the specific documents you used to formulate your answer.
                        Documents:
                        ---
                        ${documentContext}
                        ---
                        Question: "${question}"
                     `;

    const payload = {
      model: "gpt-3.5-turbo",
      messages: [
        { role: "system", content: systemPrompt },
        { role: "user", content: userPrompt }
      ],
    };

    const response = await fetch("https://api.openai.com/v1/chat/completions", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${this.apiKey}`,
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorDetails = await response.text();
      throw new Error(`OpenAI API request failed with status ${response.status}: ${errorDetails}`);
    }

    const data = await response.json();
    const choice = data.choices?.[0];

    if (!choice) {
      throw new Error("Invalid response structure from OpenAI API.");
    }

    return choice.message.content;
  }
}